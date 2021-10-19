import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreConnection {
   private Firestore db;
    private FireStoreConnection() throws IOException {
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    static FireStoreConnection connection;

    static {
        try {
            connection = new FireStoreConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("C:\\Users\\ooffe\\IdeaProjects\\finalProj\\src\\main\\java\\tests\\serviceAccount.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://ofek-7fd70.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
         db = FirestoreClient.getFirestore();
    }
    private int resCounter=2;
    public void addDataRes(String adress, String name, String owner, String pass, GeoPoint loc) throws ExecutionException, InterruptedException {
        //DocumentReference docRef = db.collection("users").document("alovelace");
        DocumentReference docRef = db.collection("Restaurants ").document(""+resCounter);
// Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("address", adress);
        data.put("location", loc);
        data.put("name", name);
        data.put("owner", owner);
        data.put("password", pass);
//asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        while(!result.isDone()){

        }
// ...
// result.get() blocks on response
        //System.out.println("Update time : " + result.get().getUpdateTime());
        resCounter++;
    }
    CollectionReference docRef;
    public void showRes(){
         docRef = db.collection("Restaurants ");
        System.out.println("show res start");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                for (DocumentChange dc : snapshot.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        System.out.println("New rest: " + dc.getDocument().getData());
                    }
                    if (dc.getType() == DocumentChange.Type.MODIFIED) {
                        System.out.println("rest modified: " + dc.getDocument().getData());
                    }
                }
            }
        });
    }
    DocumentReference docRef1;
    public void showRes1() throws ExecutionException, InterruptedException {
        docRef1 = db.collection("Restaurants ").document("h");
        System.out.println("show res1 start");
            // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef1.get();
            // ...
            // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        } else {
            System.out.println("No such document!");
        }

        docRef1.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirestoreException e) {
                if (e != null) {
                    System.err.println("Listen failed: " + e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    System.out.println("Current data: " + snapshot.getData());
                } else {
                    System.out.print("Current data: null");
                }
            }
        });
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //String adress="addr",  name="n",  owner="o",  pass="pa";
        //GeoPoint loc=new GeoPoint(80,80);
        //connection.addDataRes( adress,  name,  owner,  pass,  loc);
        connection.showRes();
        while (true) {
            Thread.sleep(1000);
        }

    }
}
