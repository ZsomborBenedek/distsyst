package be.uantwerpen.server.service;

import javax.annotation.PostConstruct;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TCPSender {

    RestTemplate template;
    final Integer port = 9000;

    public TCPSender() {
        this.template = new RestTemplate();
    }

    /**
     * Puts the resource to the url
     * 
     * @param ip       ip address of destination
     * @param function functionName/{resourceName}
     * @param resource resource to put
     */
    public ResponseEntity<String> post(String ip, String action, String resource) {
        String url = "http://" + ip + ":" + port.toString() + "/" + action;
        return template.postForEntity(url, resource, String.class);
    }

    public ResponseEntity<String> get(String ip, String action, String resource) {
        String url = "http://" + ip + ":" + port.toString() + "/" + action;
        return template.getForEntity(url + "/" + resource, String.class);
    }

    // @PostConstruct
    // public ResponseEntity<String> post() {
    //     String ip = "143.169.221.174";
    //     String action = "setNameServer";
    //     String resource = ip;
    //     String url = "http://" + ip + ":" + port.toString() + "/" + action;
    //     return template.postForEntity(url, resource, String.class);
    // }
}