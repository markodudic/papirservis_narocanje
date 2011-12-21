package org.tempuri;

public class IripEKOLProxy implements org.tempuri.IripEKOL {
  private String _endpoint = null;
  private org.tempuri.IripEKOL iripEKOL = null;
  
  public IripEKOLProxy() {
    _initIripEKOLProxy();
  }
  
  public IripEKOLProxy(String endpoint) {
    _endpoint = endpoint;
    _initIripEKOLProxy();
  }
  
  private void _initIripEKOLProxy() {
    try {
      iripEKOL = (new org.tempuri.IripEKOLserviceLocator()).getIripEKOLPort();
      if (iripEKOL != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iripEKOL)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iripEKOL)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iripEKOL != null)
      ((javax.xml.rpc.Stub)iripEKOL)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.IripEKOL getIripEKOL() {
    if (iripEKOL == null)
      _initIripEKOLProxy();
    return iripEKOL;
  }
  
  public void setDocumentData(java.lang.String adokument_data, javax.xml.rpc.holders.StringHolder aresponse) throws java.rmi.RemoteException{
    if (iripEKOL == null)
      _initIripEKOLProxy();
    iripEKOL.setDocumentData(adokument_data, aresponse);
  }
  
  
}