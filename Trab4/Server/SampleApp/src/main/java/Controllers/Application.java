package Controllers;

        import javax.ws.rs.ApplicationPath;
        import java.util.HashSet;
        import java.util.Set;

@ApplicationPath("/api")
public class Application extends javax.ws.rs.core.Application{

    private Set<Object> singletons;
    public Application(){
        singletons = new HashSet<>();
        singletons.add(new ServImpl());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
