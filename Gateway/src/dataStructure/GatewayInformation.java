package dataStructure;

import java.io.*;

public class GatewayInformation implements Serializable {
    private static final long serialVersionUID = 1L;
    private int farmId;
    private static GatewayInformation instance;

    public static GatewayInformation getInstance() throws FileNotFoundException{
        if(instance == null){
            try {
                FileInputStream fileInputStream = new FileInputStream("GatewayInformation.dat");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                instance = (GatewayInformation) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
//                System.out.println("create gateway information");
                instance = new GatewayInformation();
                throw e;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    private GatewayInformation(){
        farmId = -1;
    }

    public int getFarmId() {
        return farmId;
    }

    public synchronized void setFarmId(int farmId) {
        this.farmId = farmId;
        update();
    }

    private void update(){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("GatewayInformation.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(instance);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
