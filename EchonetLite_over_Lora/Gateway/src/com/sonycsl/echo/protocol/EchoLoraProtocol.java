package com.sonycsl.echo.protocol;

import com.pi4j.io.gpio.exception.UnsupportedBoardType;
import com.pi4j.io.serial.*;
import com.pi4j.system.SystemInfo;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoFrame;
import com.sonycsl.echo.EchoSocket;
import utilites.Helper;
import utilites.Timer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.Collection;

public class EchoLoraProtocol extends EchoProtocol {

    private final Serial serial = SerialFactory.createInstance();

    private SerialConfig config;
    private EchoLoraProtocol thisProtocol = this;
    private Timer timer = new Timer();
    public void openLora() throws IOException, InterruptedException {

        serial.addListener(new SerialDataEventListener() {
            @Override
            public void dataReceived(SerialDataEvent event) {

                // NOTE! - It is extremely important to read the data received from the
                // serial port.  If it does not get read from the receive buffer, the
                // buffer will continue to grow and consume memory.
                byte[] data;
                byte[] sourceId, destId;
                byte[] dataEcho;
                final int echoStartByte = 8;
                try {
                    data = event.getBytes();
                    serial.flush();

                    if(data.length<21) return;
                    sourceId = new byte[]{data[0],data[1],data[2],data[3]};
                    destId = new byte[]{data[4],data[5],data[6],data[7]};
                    dataEcho = new byte[data.length-echoStartByte];
                    for(int i=echoStartByte;i<data.length;i++){
                        dataEcho[i-echoStartByte] = data[i];
                    }

//                     print byte array
//                    System.out.println("RECEIVE:");
//                    System.out.println("Data: ");
//                    Helper.printBytes(data,data.length);
//                    System.out.println("SourceId:");
//                    Helper.printBytes(sourceId,sourceId.length);
//                    System.out.println("DestId:");
//                    Helper.printBytes(destId,destId.length);
//                    System.out.println("dataEcho: ");
//                    Helper.printBytes(dataEcho,dataEcho.length);
//                     end print byte array

                    InetAddress address = InetAddress.getByAddress(sourceId);
                    String srcEchoAddress = address.getHostAddress();
                    EchoFrame frame = new EchoFrame(srcEchoAddress, dataEcho);

                    EchoLoraProtocol.LoraProtocolTask task = new EchoLoraProtocol.LoraProtocolTask(frame, thisProtocol);
                    EchoSocket.enqueueTask(task);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // create serial config object
        config = new SerialConfig();

        // set default serial settings (device, baud rate, flow control, etc)
        //
        // by default, use the DEFAULT com port on the Raspberry Pi (exposed on GPIO header)
        // NOTE: this utility method will determine the default serial port for the
        //       detected platform and board/model.  For all Raspberry Pi models
        //       except the 3B, it will return "/dev/ttyAMA0".  For Raspberry Pi
        //       model 3B may return "/dev/ttyS0" or "/dev/ttyAMA0" depending on
        //       environment configuration.
        try {
            System.out.println("BoardType: " + SystemInfo.getBoardType());
            config.device(SerialPort.getDefaultPort())
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);
        } catch (UnsupportedBoardType e) {
            System.out.println("BoardType: " + SystemInfo.getBoardType());
            config.device("/dev/ttyS0")
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);
        }
    }
    @Override
    public void receive() {
        try {
            if(!serial.isOpen()){
                serial.open(config);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLora(EchoFrame frame) throws IOException {
        if(!serial.isOpen()){
            serial.open(config);
        }
        Echo.getEventListener().sendEvent(frame);
        if (frame.getDstEchoAddress().equals(EchoSocket.SELF_ADDRESS)) {
            sendToSelf(frame.copy());
            return;
        }

        //Thang cmt: dataLora: [Length: 1 byte(bo), Source ID: 4 bytes, Dest ID: 4 bytes, dataLora]
        //final int lengthStartIndex=0;
        final int sourceIDStartIndex = 0;
        final int destIDStartIndex = 4;
        byte[] dataEcho = frame.getFrameByteArray();
        byte[] dataLora = new byte[8+dataEcho.length];
        frame.getDstEchoAddress().toCharArray();
        //dataLora[lengthStartIndex]=(byte)(8+dataEcho.length);
        for(int i =0;i<4;i++){
            dataLora[i+sourceIDStartIndex]=InetAddress.getByName(frame.getSrcEchoAddress()).getAddress()[i];
        }
        for(int i=0;i<4;i++){
            dataLora[i+destIDStartIndex]=InetAddress.getByName(frame.getDstEchoAddress()).getAddress()[i];
        }
        for(int i=8;i<dataLora.length;i++){
            dataLora[i]=dataEcho[i-8];
        }

		if(timer.isCanSend()) {
//            System.out.print("SEND: ");
//            for (int i = 0; i < dataLora.length; i++) {
//                // System.out.print(data[i] + ",");
//                System.out.printf("0x%02X", dataLora[i]);
//                System.out.print(", ");
//            }

            serial.write(dataLora);
            timer.waitToReceive();
        }
//        InetAddress address = InetAddress.getByName(frame.getDstEchoAddress());
//        DatagramPacket packet = new DatagramPacket(data, data.length, address, PORT);
//
//        mMulticastSocket.send(packet);
//        if (frame.getDstEchoAddress().equals(EchoSocket.MULTICAST_ADDRESS)) {
//            EchoFrame f = frame.copy();
//            f.setDstEchoAddress(EchoSocket.SELF_ADDRESS);
//            sendToSelf(f);
//        }
    }

    public void sendToSelf(EchoFrame frame) {
        EchoLoraProtocol.LoraProtocolTask task = new EchoLoraProtocol.LoraProtocolTask(frame, thisProtocol);
        EchoSocket.enqueueTask(task);
    }

    public boolean isOpened() {
        return serial.isOpen();
    }

    public static class LoraProtocolTask extends EchoProtocol.Task{

        EchoLoraProtocol mProtocol;

        public LoraProtocolTask(EchoFrame frame, EchoLoraProtocol protocol) {
            super(frame);
            mProtocol = protocol;
        }

        @Override
        protected void respond(EchoFrame response) {
            try {
                mProtocol.sendLora(response);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void informAll(EchoFrame response) {
            try {
                mProtocol.sendLora(response);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
