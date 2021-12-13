package application.controller;

import application.controller.dao.DAO;
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
    private static final String restCol="Restaurants";
    private Firestore db;
    private ListenerRegistration liveUpdate;
    static FireStoreConnection connection;


    private FireStoreConnection() throws IOException {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // return the collection of restaurants
    private CollectionReference getMainCol(){
        return db.collection(restCol);
    }

    /**
     * 
     * @return {@link FireStoreConnection} - singleton object that use Firestore api for read/write and connect to Firestore database.
     */
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

    public void deleteData(String collection1, String document, String collection2,String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> result = db.collection(collection1).document(document).collection(collection2).document(id).delete();
        while(!result.isDone());

    }

    /**
     * 
     * @param target - the target document id to be change. if null generate id and create new document.
     * @param data - the data that change inside the target.
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public String addData(String target, Object data) throws ExecutionException, InterruptedException {
        DocumentReference doc;
        if (target==null){
             doc = getMainCol().add(null).get();
        }
        else  doc = getMainCol().document(target);
        ApiFuture res = addData(doc, data);
        while (!res.isDone()) {
        }
        return doc.getId();
    }
    /**
     *
     * @param target - the target document id to be change. if null generate id and create new document.
     * @param data - the data that change inside the target.
     * @param collection - the collection inside the document.
     * @param document - the document that hold collection.
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public boolean addData(String document,String collection,String target,Object data ) throws ExecutionException, InterruptedException {
        DocumentReference doc;
        if (target==null){
            doc = getMainCol().document(document).collection(collection).add(null).get();
        }
        else  doc=getMainCol().document(document).collection(collection).document(target);

        ApiFuture res = addData(doc, data);
        while (!res.isDone()) {
        }
        return true;
    }
    private ApiFuture addData(DocumentReference doc,Object data){
        Map<String, Object> map = fromObjectToMap(data);
        Object obj = map.get("collections");
        if (obj != null){
            HashMap<String, Object> collections= (HashMap<String, Object>) obj;
            map.remove("collections");
            ApiFuture<WriteResult> result = doc.set(map);
            collections.forEach((key,val)->{
                doc.collection("data").document(key).set(val);
            });
            return result;
        }
        return doc.set(map);
    }
    public void stopLiveUpdate(){
        liveUpdate.remove();
    }

    /**
     * connect listener to update gui when data in Firestore change (live update). 
     * @param doc
     * @param coll
     * @param listener
     */
    public void connectListenerToData(String doc, String coll, DAO listener) {
        if (liveUpdate!=null) liveUpdate.remove(); // TODO: array of liveUpdate (restaurant + requests)
        CollectionReference colRef = getMainCol().document(doc).collection(coll);
        liveUpdate = colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            //System.out.println("New city: " + dc.getDocument().getData());
                            listener.onDataAdded(dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            //System.out.println("Modified city: " + dc.getDocument().getData());
                            listener.onDataChanged(dc.getDocument().getData());
                            break;
                        case REMOVED:
                            //System.out.println("Removed city: " + dc.getDocument().getData());
                            listener.onDataRemoved(dc.getDocument().getData());
                            break;
                        default:
                            break;
                    }
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
    public <T> ArrayList<T> getAllData(String document,String collection,T output) throws ExecutionException, InterruptedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ApiFuture<QuerySnapshot> future = getMainCol().document(document).collection(collection).get();
        future.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        ArrayList listObjects=new ArrayList<T>();
        for (QueryDocumentSnapshot doc : documents) {
            listObjects.add(fromMapToObject(doc.getData(),output.getClass().getConstructor().newInstance()));
        }
        return listObjects;
    }
    public <T> T getDataById(String id,String document,String collection,T output) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getMainCol().document(document).collection(collection).document(id);
        return getDataById(docRef,output);
    }
    public <T> T getDataById(String id,T output) throws ExecutionException, InterruptedException {
        DocumentReference docRef = getMainCol().document(id);
        return getDataById(docRef,output);
    }
    public Map<String, Object> fromObjectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Map<String,Object> collections=new HashMap<>();
        if (obj instanceof ArrayList){
            ((ArrayList) obj).forEach(e -> {
                ArrayList arrayList= (ArrayList) map.get(e.getClass().getSimpleName());
                if (arrayList==null) {
                    arrayList=new ArrayList();
                    map.put(e.getClass().getSimpleName(),arrayList);
                }
                arrayList.add(e);
            });
            return map;
        }
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
                        hashMap.put(e.getClass().getSimpleName(), fromObjectToMap(e));
                    });
                } else if (object instanceof ArrayList) {
                    // array list not added to the database.
                    /* // with collection:
                    map.put("collections",collections);
                    collections.put(field.getName(),fromObjectToMap(object));
                    */
                    /* // without collections:
                    ArrayList arrayList;
                    map.put(field.getName(),arrayList =  new ArrayList());
                    ArrayList hashSet = (ArrayList) object;
                    hashSet.stream().forEach(e->{
                        HashMap hashMap;
                        arrayList.add(hashMap=new HashMap());
                        hashMap.put(e.getClass().getSimpleName(), fromObjectToMap(e));
                    });
                    */
                }else map.put(field.getName(), field.get(obj));
            } catch (Exception e) {
            }
        }
        return map;
    }

    public <T> T fromMapToObject(Map<String, Object> map, T output) {
        /* @@TODO need to fix convert:
         tables
        */
        if (map==null) return output;
        if (output instanceof ArrayList){
            map.forEach((key,val)->{
                ((ArrayList)val).forEach(el->{
                    try {
                        Class<?> clazz = Class.forName("application.entities."+(String)key);
                        Constructor<?> ctor = clazz.getConstructor();
                        Object object = ctor.newInstance();
                        ((ArrayList) output).add(fromMapToObject((HashMap)el,object));
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
        }
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

