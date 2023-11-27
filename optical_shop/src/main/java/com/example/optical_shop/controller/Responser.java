package com.example.optical_shop.controller;

import com.example.optical_shop.dto.ResponseDto;

public class Responser {
    public static ResponseDto getResponse(String message){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }
}
