package exclude;

import java.util.List;

public abstract class AbstractServiceProvider implements ServiceProvider {

    public abstract List<ServiceData> getItemlist();
    public abstract ServiceData agolithem(List<ServiceData> serviceDatas);
    @Override
    public ServiceData getServiceByName() {
        return agolithem(getItemlist());
    }
}
