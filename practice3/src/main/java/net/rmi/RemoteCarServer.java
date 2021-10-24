package net.rmi;

import net.command.SerializableCommand;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCarServer extends Remote{
    <T> T executeCommand(SerializableCommand command) throws RemoteException;
}