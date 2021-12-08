package application.entities;

import java.util.Objects;

public class Request {

        private String client_id,time;
        private Long table_id;
        private boolean approved;

        public Request(String client_id, Long table_id, String time, boolean approved) {
            this.client_id = client_id;
            this.table_id = table_id;
            this.time = time;
            this.approved = approved;
        }
        public Request(){
            this("no id",0L,"no time",false);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return getClient_id().equals(request.getClient_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClient_id());
    }
}
