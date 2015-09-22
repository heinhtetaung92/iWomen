package org.undp_iwomen.iwomen.data;

import java.io.Serializable;

/**
 * Created by khinsandar on 8/13/15.
 */
public class TlgProfileItem implements Serializable {

    private String _objectId;
    private String _tlg_group_name;
    private String _tlg_group_address;
    private String _tlg_group_lat_address;
    private String _tlg_group_lng_address;

    public TlgProfileItem(String objectId, String tlg_group_name, String tlg_group_address, String tlg_group_lat_address, String tlg_group_lng_address){
        this._objectId = objectId;
        this._tlg_group_name = tlg_group_name;
        this._tlg_group_address = tlg_group_address;
        this._tlg_group_lat_address = tlg_group_lat_address;
        this._tlg_group_lng_address = tlg_group_lng_address;
    }

    public String get_objectId() {
        return _objectId;
    }

    public void set_objectId(String _objectId) {
        this._objectId = _objectId;
    }

    public String get_tlg_group_name() {
        return _tlg_group_name;
    }

    public void set_tlg_group_name(String _tlg_group_name) {
        this._tlg_group_name = _tlg_group_name;
    }

    public String get_tlg_group_address() {
        return _tlg_group_address;
    }

    public void set_tlg_group_address(String _tlg_group_address) {
        this._tlg_group_address = _tlg_group_address;
    }

    public String get_tlg_group_lat_address() {
        return _tlg_group_lat_address;
    }

    public void set_tlg_group_lat_address(String _tlg_group_lat_address) {
        this._tlg_group_lat_address = _tlg_group_lat_address;
    }

    public String get_tlg_group_lng_address() {
        return _tlg_group_lng_address;
    }

    public void set_tlg_group_lng_address(String _tlg_group_lng_address) {
        this._tlg_group_lng_address = _tlg_group_lng_address;
    }
}
