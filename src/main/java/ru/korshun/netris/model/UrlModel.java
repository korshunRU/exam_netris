package ru.korshun.netris.model;

import lombok.Value;

@Value
public class UrlModel {
  int id;
  String sourceDataUrl;
  String tokenDataUrl;
}
