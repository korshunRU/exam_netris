package ru.korshun.netris.service;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.korshun.netris.model.ResponseModel;
import ru.korshun.netris.model.UrlModel;
import ru.korshun.netris.utils.AsyncHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CamService {

  @Value("${ru.korshun.target}")
  private String url;

  private final AsyncHandler asyncHandler;

  @Autowired
  public CamService(AsyncHandler asyncHandler) {
    this.asyncHandler = asyncHandler;
  }

  public List<ResponseModel> dataProccess() throws IOException {

    List<CompletableFuture<ResponseModel>> futures = new ArrayList<>();
    String inputArray = asyncHandler.getStringFromUrl(url).orElse("[]");
    JSONArray json = new JSONArray(inputArray);

    IntStream
            .range(0, json.length())
            .mapToObj(i -> new Gson().fromJson(json.get(i).toString(), UrlModel.class))
            .forEach(i -> {
              try {
                futures.add(asyncHandler.getResult(i.getId(), i.getSourceDataUrl(), i.getTokenDataUrl()));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });

    return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
  }

}
