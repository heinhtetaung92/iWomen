package org.undp_iwomen.iwomen.data;

/**
 * Created by khinsandar on 8/28/15.
 */
public class SisterAppItem {

    private String _app_name;
    private int _app_img;
    private String _app_down_link;

    public SisterAppItem(String app_name, String app_down_link, int img_app){
        this._app_name = app_name;
        this._app_down_link = app_down_link;
        this._app_img = img_app;
    }

    public String get_app_name() {
        return _app_name;
    }

    public void set_app_name(String _app_name) {
        this._app_name = _app_name;
    }

    public int get_app_img() {
        return _app_img;
    }

    public void set_app_img(int _app_img) {
        this._app_img = _app_img;
    }

    public String get_app_down_link() {
        return _app_down_link;
    }

    public void set_app_down_link(String _app_down_link) {
        this._app_down_link = _app_down_link;
    }
}
