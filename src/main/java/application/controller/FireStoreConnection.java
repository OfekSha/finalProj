package application.controller;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FireStoreConnection {

    // Data is object that will use to save data and build map.
    public class Data {
        private String name;
        private Object data;

        public Data(String name, Object data) {
            this.name = name;
            this.data = data;
        }
    }


    private CollectionReference docRef;
    private Firestore db;

    private FireStoreConnection() throws IOException {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static FireStoreConnection connection;

    // singleton:
    static public FireStoreConnection getDB() {
        if (connection!=null) return connection;
        try {
            connection = new FireStoreConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void init() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("application/json/serviceAccount.json");

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ofek-7fd70.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    private int resCounter = 2;


    public void addDataRes(String collection, String document, Data... data) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(collection).document(document);
        Map<String, Object> map = new HashMap<>();
        for (Data d : data) {
            map.put(d.name, d.data);
        }
        ApiFuture<WriteResult> result = docRef.set(map);
        while (!result.isDone()) {
        }
    }

    public void updateData(String collection, String document, Object data) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection(collection).document(document);
        Map<String, Object> map = parameters(data);
        ApiFuture<WriteResult> result = docRef.set(map);
        while (!result.isDone()) {
        }
    }

    public String addDataRes(String collection, Object data) throws ExecutionException, InterruptedException {
        Map<String, Object> map = parameters(data);
        ApiFuture<DocumentReference> result = db.collection(collection).add(map);
        while (!result.isDone()) {
        }
        return result.get().getId();
    }
    public boolean addDataRes(String collection1,String document,String collection2,String target,Object data ) throws ExecutionException, InterruptedException {
        Map<String, Object> map = parameters(data);
        ApiFuture<WriteResult> result = db.collection(collection1).document(document).collection(collection2).document(target).set(map);
        while (!result.isDone()) {
        }
        return true;
    }
    public void connectListenerToData(String id,FireStoreListener listener) {
        DocumentReference docRef = db.collection("Restaurants").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    //System.out.println("Current data: " + snapshot.getData());
                    listener.onDataChanged(snapshot.getData());
                } else {
                    System.out.print("Current data: null");
                }
            }
        });
    }
    private <T> T getDataById(DocumentReference docRef,T output) throws ExecutionException, InterruptedException {
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // ...
        // future.get() blocks on response
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            //System.out.println("Document data: " + document.getData());
        } else {
            //System.out.println("No such document!");
        }
        while (!future.isDone()) ;
        return fromMapToObject(future.get().getData(),output);
    }
    public <T> List<T> getAllData(String document,String collection,T output) throws ExecutionException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ApiFuture<QuerySnapshot> future = db.collection("Restaurants").document(document).collection(collection).get();
        future.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List listObjects=new ArrayList<T>();
        for (QueryDocumentSnapshot doc : documents) {
            listObjects.add(fromMapToObject(doc.getData(),output.getClass().getConstructor().newInstance()));
        }
        return listObjects;
    }
    public <T> T getDataById(String id,String document,String collection,T output) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Restaurants").document(document).collection(collection).document(id);
        return getDataById(docRef,output);
    }
    public <T> T getDataById(String id,T output) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection("Restaurants").document(id);
        return getDataById(docRef,output);
    }

    private Map<String, Object> parameters(Object obj) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object object = field.get(obj);
                if (object instanceof HashSet) {
                    ArrayList arrayList;
                    map.put(field.getName(),arrayList =  new ArrayList());
                    HashSet hashSet = (HashSet) object;
                    hashSet.stream().forEach(e->{
                        HashMap hashMap;
                        arrayList.add(hashMap=new HashMap());
                        hashMap.put(e.getClass().getSimpleName(),parameters(e));
                    });
                } else if (object instanceof ArrayList) {
                    ArrayList arrayList;
                    map.put(field.getName(),arrayList =  new ArrayList());
                    ArrayList hashSet = (ArrayList) object;
                    hashSet.stream().forEach(e->{
                        HashMap hashMap;
                        arrayList.add(hashMap=new HashMap());
                        hashMap.put(e.getClass().getSimpleName(),parameters(e));
                    });
                }else map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
            }
        }
        return map;
    }

    private <T> T fromMapToObject(Map<String, Object> map, T output) {
        /* @@TODO need to fix convert:
         tables
        */
        if (map==null) return output;
        for (Field field : output.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object obj = map.get(field.getName());
                if (obj==null) continue;
                if (obj instanceof HashMap && !field.getType().isAssignableFrom(HashMap.class)){ // convert object
                    field.set(output,fromMapToObject((HashMap)obj,field.get(output)));
                }
                else if (obj instanceof ArrayList && field.getType().isAssignableFrom(HashSet.class)) { // convert array
                    HashSet hashSet = (HashSet) field.get(output);
                    ArrayList arrayList = (ArrayList) obj;
                    arrayList.stream().forEach(e -> {
                        if (e instanceof HashMap)
                            ((HashMap) e).forEach((key, val) -> {
                                try {
                                    Class<?> clazz = Class.forName("application.entities."+(String)key);
                                    Constructor<?> ctor = clazz.getConstructor();
                                    Object object = ctor.newInstance();
                                    hashSet.add(fromMapToObject((HashMap)val,object));
                                } catch (ClassNotFoundException | NoSuchMethodException ex) {
                                    ex.printStackTrace();
                                } catch (InvocationTargetException invocationTargetException) {
                                    invocationTargetException.printStackTrace();
                                } catch (InstantiationException instantiationException) {
                                    instantiationException.printStackTrace();
                                } catch (IllegalAccessException illegalAccessException) {
                                    illegalAccessException.printStackTrace();
                                }
                            });
                    });
                    field.set(output, hashSet);
                }
                else if (obj instanceof ArrayList && field.getType().isAssignableFrom(ArrayList.class)) { // convert array
                    ArrayList hashSet = (ArrayList) field.get(output);
                    ArrayList arrayList = (ArrayList) obj;
                    arrayList.stream().forEach(e -> {
                        if (e instanceof HashMap)
                            ((HashMap) e).forEach((key, val) -> {
                                try {
                                    Class<?> clazz = Class.forName("application.entities."+(String)key);
                                    Constructor<?> ctor = clazz.getConstructor();
                                    Object object = ctor.newInstance();
                                    hashSet.add(fromMapToObject((HashMap)val,object));
                                } catch (ClassNotFoundException | NoSuchMethodException ex) {
                                    ex.printStackTrace();
                                } catch (InvocationTargetException invocationTargetException) {
                                    invocationTargetException.printStackTrace();
                                } catch (InstantiationException instantiationException) {
                                    instantiationException.printStackTrace();
                                } catch (IllegalAccessException illegalAccessException) {
                                    illegalAccessException.printStackTrace();
                                }
                            });
                    });
                    field.set(output, hashSet);
                }
                else if(obj instanceof Long && field.getType().isPrimitive()){ // if object is Long

                    field.set(output,((Long)obj).intValue());
                }
                else if (field.getType().isEnum()){
                    field.set(output, Enum.valueOf((Class<Enum>)field.get(output).getClass(),(String)obj));
                }
                 else field.set(output,obj); // primitive variable
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }
}

