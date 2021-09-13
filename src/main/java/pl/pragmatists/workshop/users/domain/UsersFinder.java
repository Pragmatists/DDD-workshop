package pl.pragmatists.workshop.users.domain;

public interface UsersFinder {

    UserProjection byId(String id);

    class UserProjection {
        public String id;
        public Email email;

        public static class Email {
            public String email;
        }
    }

}

