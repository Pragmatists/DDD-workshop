package pl.pragmatists.workshop.users.infrastructure;

//@Component
public class InMemoryUserRepository {

//    private ArrayList<User> users = new ArrayList<>();
//
//    @Override
//    public void save(User user) {
//        findUser(user.id)
//                .ifPresentOrElse($ -> {
//                    users.remove($);
//                    users.add(user);
//                }, () -> users.add(user));
//    }
//
//    @Override
//    public User load(String id) {
//        return findUser(id)
//                .get();
//    }
//
//    @Override
//    public User find(String email) {
//        return users.stream().filter($ -> $.email.value().equals(email)).findFirst().get();
//    }
//
//    @Override
//    public boolean hasUserWith(String email) {
//        return users.stream().anyMatch(user -> user.email.value().equals(email));
//    }
//
//    private Optional<User> findUser(String id) {
//        return users.stream().filter(user -> user.id.equals(id))
//                .findFirst();
//    }
//
//    @Override
//    public void clearAll() {
//        users.clear();
//    }
}
