/**
 * IripEKOLserviceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class IripEKOLserviceLocator extends org.apache.axis.client.Service implements org.tempuri.IripEKOLservice {

    public IripEKOLserviceLocator() {
    }


    public IripEKOLserviceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IripEKOLserviceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IripEKOLPort
    private java.lang.String IripEKOLPort_address = "http://dagama.vasco.si/ekol/ekol.dll/soap/IripEKOL";

    public java.lang.String getIripEKOLPortAddress() {
        return IripEKOLPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IripEKOLPortWSDDServiceName = "IripEKOLPort";

    public java.lang.String getIripEKOLPortWSDDServiceName() {
        return IripEKOLPortWSDDServiceName;
    }

    public void setIripEKOLPortWSDDServiceName(java.lang.String name) {
        IripEKOLPortWSDDServiceName = name;
    }

    public org.tempuri.IripEKOL getIripEKOLPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IripEKOLPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIripEKOLPort(endpoint);
    }

    public org.tempuri.IripEKOL getIripEKOLPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.IripEKOLbindingStub _stub = new org.tempuri.IripEKOLbindingStub(portAddress, this);
            _stub.setPortName(getIripEKOLPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIripEKOLPortEndpointAddress(java.lang.String address) {
        IripEKOLPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.IripEKOL.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.IripEKOLbindingStub _stub = new org.tempuri.IripEKOLbindingStub(new java.net.URL(IripEKOLPort_address), this);
                _stub.setPortName(getIripEKOLPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IripEKOLPort".equals(inputPortName)) {
            return getIripEKOLPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "IripEKOLservice");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "IripEKOLPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IripEKOLPort".equals(portName)) {
            setIripEKOLPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
