package ru.korshun.netris.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResponseModel {
  private int id;
  private String type;
  private String url;
  private String value;
  private int ttl;
}
