package com.android.otainstaller;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IInstall extends IInterface {
    public static final int STAT_FISCAL_SERVICE_ERRORS = 5;
    public static final int STAT_FISCAL_SERVICE_STARTS = 4;
    public static final int STAT_PINPAD_SERVICE_ERRORS = 2;
    public static final int STAT_PINPAD_SERVICE_REBOOTS = 3;
    public static final int STAT_PINPAD_SERVICE_STARTS = 1;

    /* loaded from: classes.dex */
    public static class Default implements IInstall {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getIccid() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getImei() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getImsi() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getNetworkOperatorName() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getPhoneNumber() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getProductVersion() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getSerialNumber() throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public String getStat(int i) throws RemoteException {
            return null;
        }

        @Override // com.android.otainstaller.IInstall
        public void installOta(String str) throws RemoteException {
        }
    }

    String getIccid() throws RemoteException;

    String getImei() throws RemoteException;

    String getImsi() throws RemoteException;

    String getNetworkOperatorName() throws RemoteException;

    String getPhoneNumber() throws RemoteException;

    String getProductVersion() throws RemoteException;

    String getSerialNumber() throws RemoteException;

    String getStat(int i) throws RemoteException;

    void installOta(String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IInstall {
        private static final String DESCRIPTOR = "com.android.otainstaller.IInstall";
        static final int TRANSACTION_getIccid = 6;
        static final int TRANSACTION_getImei = 4;
        static final int TRANSACTION_getImsi = 5;
        static final int TRANSACTION_getNetworkOperatorName = 8;
        static final int TRANSACTION_getPhoneNumber = 7;
        static final int TRANSACTION_getProductVersion = 3;
        static final int TRANSACTION_getSerialNumber = 2;
        static final int TRANSACTION_getStat = 9;
        static final int TRANSACTION_installOta = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInstall asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface != null && (queryLocalInterface instanceof IInstall)) {
                return (IInstall) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    installOta(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    String serialNumber = getSerialNumber();
                    parcel2.writeNoException();
                    parcel2.writeString(serialNumber);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    String productVersion = getProductVersion();
                    parcel2.writeNoException();
                    parcel2.writeString(productVersion);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    String imei = getImei();
                    parcel2.writeNoException();
                    parcel2.writeString(imei);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    String imsi = getImsi();
                    parcel2.writeNoException();
                    parcel2.writeString(imsi);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    String iccid = getIccid();
                    parcel2.writeNoException();
                    parcel2.writeString(iccid);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    String phoneNumber = getPhoneNumber();
                    parcel2.writeNoException();
                    parcel2.writeString(phoneNumber);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    String networkOperatorName = getNetworkOperatorName();
                    parcel2.writeNoException();
                    parcel2.writeString(networkOperatorName);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    String stat = getStat(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeString(stat);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IInstall {
            public static IInstall sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.android.otainstaller.IInstall
            public void installOta(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().installOta(str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getSerialNumber() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSerialNumber();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getProductVersion() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProductVersion();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getImei() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImei();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getImsi() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getImsi();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getIccid() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getIccid();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getPhoneNumber() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPhoneNumber();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getNetworkOperatorName() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getNetworkOperatorName();
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.android.otainstaller.IInstall
            public String getStat(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStat(i);
                    }
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInstall iInstall) {
            if (Proxy.sDefaultImpl == null) {
                if (iInstall == null) {
                    return false;
                }
                Proxy.sDefaultImpl = iInstall;
                return true;
            }
            throw new IllegalStateException("setDefaultImpl() called twice");
        }

        public static IInstall getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
