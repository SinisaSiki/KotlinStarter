package com.telpo.tps550.api.led;

import android.os.Handler;
import android.util.Log;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.serial.Serial;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class SerialUtil {
    private static final String Tag = "SerialUtil";
    private final Handler handler;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ByteBuffer rcvBuffer = ByteBuffer.allocate(4096);
    private ReadThread readThread;
    private Serial serialPort;

    public SerialUtil(Handler handler) {
        this.handler = handler;
    }

    /* loaded from: classes.dex */
    private class ReadThread extends Thread {
        private ReadThread() {
            SerialUtil.this = r1;
        }

        /* synthetic */ ReadThread(SerialUtil serialUtil, ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public synchronized void run() {
            int read;
            byte[] bArr = new byte[1024];
            Log.d(SerialUtil.Tag, "Read thread start!");
            super.run();
            while (!isInterrupted()) {
                try {
                    sleep(200L);
                    if (SerialUtil.this.inputStream.available() > 0 && (read = SerialUtil.this.inputStream.read(bArr)) > 0) {
                        synchronized (SerialUtil.this.rcvBuffer) {
                            if (SerialUtil.this.rcvBuffer.hasRemaining()) {
                                if (SerialUtil.this.rcvBuffer.remaining() < read) {
                                    read = SerialUtil.this.rcvBuffer.remaining();
                                }
                                SerialUtil.this.rcvBuffer.put(bArr, 0, read);
                            }
                        }
                        SerialUtil.this.handler.sendEmptyMessage(1);
                    }
                } catch (Exception e) {
                    Log.d(SerialUtil.Tag, "Read thread end for IOException!");
                    e.printStackTrace();
                }
            }
            Log.d(SerialUtil.Tag, "Read thread end!");
        }
    }

    public void open(String str, int i) throws SecurityException, IOException {
        if (this.serialPort == null) {
            Log.i(Tag, str);
            try {
                this.serialPort = new Serial(str, i, 0);
            } catch (DeviceAlreadyOpenException e) {
                e.printStackTrace();
            }
            if (this.serialPort == null) {
                return;
            }
            if (this.readThread == null) {
                ReadThread readThread = new ReadThread(this, null);
                this.readThread = readThread;
                readThread.start();
            }
            this.inputStream = this.serialPort.getInputStream();
            this.outputStream = this.serialPort.getOutputStream();
            Log.d(Tag, "Open serial port OK!");
        }
    }

    public void close() {
        ReadThread readThread = this.readThread;
        if (readThread != null) {
            readThread.interrupt();
            this.readThread = null;
        }
        InputStream inputStream = this.inputStream;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.d(Tag, "inputStream close failed!");
                e.printStackTrace();
            }
            this.inputStream = null;
        }
        OutputStream outputStream = this.outputStream;
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e2) {
                Log.d(Tag, "outputStream close failed!");
                e2.printStackTrace();
            }
            this.outputStream = null;
        }
        Serial serial = this.serialPort;
        if (serial != null) {
            serial.close();
            this.serialPort = null;
        }
        Log.d(Tag, "close serial port OK!");
    }

    public void send(byte[] bArr) {
        if (this.outputStream != null) {
            for (byte b : bArr) {
                try {
                    this.outputStream.write(b);
                    this.outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public int get(byte[] bArr) {
        int i;
        synchronized (this.rcvBuffer) {
            this.rcvBuffer.flip();
            i = 0;
            if (this.rcvBuffer.hasRemaining()) {
                int remaining = this.rcvBuffer.remaining() < bArr.length ? this.rcvBuffer.remaining() : bArr.length;
                this.rcvBuffer.get(bArr, 0, remaining);
                i = remaining;
            }
            this.rcvBuffer.compact();
        }
        return i;
    }

    public boolean isOpen() {
        return this.serialPort != null;
    }
}
