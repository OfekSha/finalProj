package application.entities;

public class Request {

        private String client_id, table_id, time;
        private boolean approved;

        public Request(String client_id, String table_id, String time, boolean approved) {
            this.client_id = client_id;
            this.table_id = table_id;
            this.time = time;
            this.approved = approved;
        }

}
