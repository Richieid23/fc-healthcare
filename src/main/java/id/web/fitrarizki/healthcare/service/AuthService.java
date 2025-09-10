package id.web.fitrarizki.healthcare.service;

import id.web.fitrarizki.healthcare.dto.AuthReq;
import id.web.fitrarizki.healthcare.dto.UserInfo;

public interface AuthService {
    UserInfo authenticate(AuthReq authReq);
}
