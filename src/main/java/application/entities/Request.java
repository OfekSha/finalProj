package application.entities;

public class Request {

        private String client_id;
        private Long table_id, time;
        private boolean approved;

        public Request(String client_id, Long table_id, Long time, boolean approved) {
            this.client_id = client_id;
            this.table_id = table_id;
            this.time = time;
            this.approved = approved;
        }
        public Request(){
            this("no id",0L,0L,false);
        }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Long getTable_id() {
        return table_id;
    }

    public void setTable_id(Long table_id) {
        this.table_id = table_id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
