package org.wso2.extensions.siddhi.io.file;

import org.wso2.carbon.messaging.ServerConnector;
import org.wso2.carbon.transport.file.connector.server.FileServerConnectorProvider;
import org.wso2.carbon.transport.filesystem.connector.server.FileSystemServerConnectorProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class FileSourceServiceProvider {
    private static FileSourceServiceProvider fileSourceServiceProvider = new FileSourceServiceProvider();
    private static FileServerConnectorProvider fileServerConnectorProvider;
    private static FileSystemServerConnectorProvider fileSystemServerConnectorProvider;
    private static int serverConnectorCount = 0;
    private static int systemServerConnectorCount = 0;
    private static int vfsClientConnectorCount = 0;
    private static ArrayList<String> serverConnectorIDs;
    private static ArrayList<String> systemServerConnectorIDs;
    private final static String SYSTEM_SERVER_CONNECTOR_ID_PREFEX = "file-system-server-connector-";
    private final static String SERVER_CONNECTOR_ID_PREFEX = "file-server-connector-";
    private final static String VFS_CLIENT_CONNECTOR_ID_PREFEX = "vfs-client-connector-";
    private Map<String,Long> filePointerMap;
    private List<Thread> serverConnectors;

    private FileSourceServiceProvider(){
        fileServerConnectorProvider = new FileServerConnectorProvider();
        fileSystemServerConnectorProvider = new FileSystemServerConnectorProvider();
        serverConnectorIDs = new ArrayList<>();
        systemServerConnectorIDs = new ArrayList<>();
        filePointerMap = new HashMap<>();
        serverConnectors = new ArrayList<>();
    }

    public static FileSourceServiceProvider getInstance(){
        return fileSourceServiceProvider;
    }

    public  FileServerConnectorProvider getFileServerConnectorProvider(){
        return fileServerConnectorProvider;
    }

    public  FileSystemServerConnectorProvider getFileSystemServerConnectorProvider(){
        return fileSystemServerConnectorProvider;
    }

    public  String getServerConnectorID(){
        String id = SERVER_CONNECTOR_ID_PREFEX + Integer.toString(serverConnectorCount++);
        serverConnectorIDs.add(id);
        return id;
    }

    public String getSystemServerConnectorID(){
        String id = SYSTEM_SERVER_CONNECTOR_ID_PREFEX + Integer.toString(systemServerConnectorCount++);
        systemServerConnectorIDs.add(id);
        return id;
    }

    public String getVFSClientConnectorID(){
        return VFS_CLIENT_CONNECTOR_ID_PREFEX + Integer.toString(vfsClientConnectorCount++);
    }

    public ArrayList<String> getServerConnectorIDList(){
        return serverConnectorIDs;
    }

    public ArrayList<String>  getSystemServerConnectorIDList(){
        return systemServerConnectorIDs;
    }

    public  Map<String, Long> getFilePointerMap(){
        return filePointerMap;
    }

    public  void updateFilePointer(String id, int readBytes){
        Long fp = getFilePointer(id);
        filePointerMap.put(id, fp+ readBytes);
        System.err.println("*******************************"+(fp+readBytes));
    }

    public Long getFilePointer(String id) {
        if(filePointerMap.containsKey(id)){
            return filePointerMap.get(id);
        }
        return new Long(0);
    }

    public void addServerConnector(Thread serverConnectorThread){
        serverConnectors.add(serverConnectorThread);
    }

    public List<Thread> getServerConnectorList(){
        return serverConnectors;
    }

    public void reset(){
        serverConnectorIDs.clear();
        systemServerConnectorIDs.clear();
        serverConnectors.clear();
        vfsClientConnectorCount = 0;
        serverConnectorCount = 0;
        systemServerConnectorCount = 0;
    }


}
