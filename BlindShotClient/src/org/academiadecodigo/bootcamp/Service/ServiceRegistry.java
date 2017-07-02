package org.academiadecodigo.bootcamp.Service;

import java.util.HashMap;

/**
 * Created by codecadet on 02/07/17.
 */
public class ServiceRegistry {
    private static HashMap<String, Service> myServices;
    private static ServiceRegistry ourInstance = new ServiceRegistry();

    public static ServiceRegistry getInstance() {
        return ourInstance;
    }

    private ServiceRegistry() {
        myServices = new HashMap<String, Service>();
        MessageService messageService = new MessageService();
        new Thread(messageService).start();
        myServices.put("Mensage", messageService);
    }
    public static Service getService(String service) {
        return myServices.get(service);
    }
}
