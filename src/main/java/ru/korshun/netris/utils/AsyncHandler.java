package ru.korshun.netris.utils;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import ru.korshun.netris.model.ResponseModel;
import ru.korshun.netris.model.SourceDataModel;
import ru.korshun.netris.model.TokenDataModel;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Component
public class AsyncHandler {

  @Async
  public <T> CompletableFuture<T> getDataFromUrl(String url, Class<T> type) throws IOException {

    String inputJson = getStringFromUrl(url).orElse("");
    T tokenData = new Gson().fromJson(inputJson, type);

    return new AsyncResult<>(tokenData).completable();
  }

  @Async
  public CompletableFuture<ResponseModel> getResult(int id, String sourceDataUrl, String tokenDataUrl) throws IOException {

    CompletableFuture<SourceDataModel> sourceData =
            getDataFromUrl(sourceDataUrl, SourceDataModel.class);
    CompletableFuture<TokenDataModel> tokenData =
            getDataFromUrl(tokenDataUrl, TokenDataModel.class);

    ResponseModel responseModel = new ResponseModel();
    responseModel.setId(id);

    Stream.of(sourceData, tokenData).map(CompletableFuture::join).forEach(i -> {
      if (i instanceof SourceDataModel) {
        responseModel.setType(((SourceDataModel) i).getUrlType());
        responseModel.setUrl(((SourceDataModel) i).getVideoUrl());
      } else if (i instanceof TokenDataModel) {
        responseModel.setValue(((TokenDataModel) i).getValue());
        responseModel.setTtl(((TokenDataModel) i).getTtl());
      }
    });

    return new AsyncResult<>(responseModel).completable();
  }

  public Optional<String> getStringFromUrl(String url) throws IOException {
    return Optional.of(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
  }

}
