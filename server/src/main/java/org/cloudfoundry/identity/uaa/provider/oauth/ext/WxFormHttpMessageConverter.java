package org.cloudfoundry.identity.uaa.provider.oauth.ext;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;

public class WxFormHttpMessageConverter extends FormHttpMessageConverter {

    public WxFormHttpMessageConverter() {
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        setSupportedMediaTypes(mediaTypes);
    }
}