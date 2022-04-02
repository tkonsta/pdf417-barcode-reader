package de.tkonsta.pdf417reader.web;

import de.tkonsta.pdf417reader.Pdf417ReaderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ResultControllerTest {

  static Pdf417ReaderService pdf417ReaderServiceMockDefault;
  static MultipartFile multipartFileMockDefault;

  private static final String DEFAULT_BARCODE_CONTENT = "content";

  @BeforeAll
  static void before() {
    pdf417ReaderServiceMockDefault = Mockito.mock(Pdf417ReaderService.class);
    when(pdf417ReaderServiceMockDefault.readPdf417BarcodeFromPdf(any())).thenReturn(DEFAULT_BARCODE_CONTENT);
    multipartFileMockDefault = Mockito.mock(MultipartFile.class);
    when(multipartFileMockDefault.isEmpty()).thenReturn(false);
  }

  @ParameterizedTest
  @MethodSource("createUploadFileParameters")
  void uploadFile(Pdf417ReaderService pdf417ReaderService,
                  MultipartFile file,
                  Boolean invalidFile,
                  Boolean noBarcodeContent,
                  String content) {

    ResultController resultController = new ResultController(pdf417ReaderService);
    ModelAndView modelAndView = resultController.uploadFile(file);
    Map<String, Object> model = modelAndView.getModel();

    if (Boolean.TRUE.equals(invalidFile)) {
      assertThat(model).containsKey("invalidFile");
      assertThat(model.get("invalidFile")).isEqualTo(Boolean.TRUE);
    } else {
      assertThat(model).doesNotContainKey("invalidFile");
    }

    if (Boolean.TRUE.equals(noBarcodeContent)) {
      assertThat(model).containsKey("noBarcodeContent");
      assertThat(model.get("noBarcodeContent")).isEqualTo(noBarcodeContent);
    } else {
      assertThat(model).doesNotContainKey("noBarcodeContent");
    }

    if (content != null) {
      assertThat(model).containsKey("content");
      assertThat(model.get("content")).isEqualTo(content);
    } else {
      assertThat(model).doesNotContainKey("content");
    }
  }

  private static Stream<Arguments> createUploadFileParameters() {
    MultipartFile multipartFileMockEmpty = Mockito.mock(MultipartFile.class);
    when(multipartFileMockEmpty.isEmpty()).thenReturn(true);

    Pdf417ReaderService pdf417ReaderServiceMockEmpty = Mockito.mock(Pdf417ReaderService.class);
    when(pdf417ReaderServiceMockEmpty.readPdf417BarcodeFromPdf(any())).thenReturn("");

    Pdf417ReaderService pdf417ReaderServiceMockException = Mockito.mock(Pdf417ReaderService.class);
    when(pdf417ReaderServiceMockException.readPdf417BarcodeFromPdf(any())).thenThrow(new RuntimeException());

    return Stream.of(
      Arguments.of(pdf417ReaderServiceMockDefault, multipartFileMockEmpty, Boolean.TRUE, Boolean.FALSE, null),
      Arguments.of(pdf417ReaderServiceMockEmpty, multipartFileMockDefault, Boolean.FALSE, Boolean.TRUE, null),
      Arguments.of(pdf417ReaderServiceMockDefault, multipartFileMockDefault, Boolean.FALSE, Boolean.FALSE, DEFAULT_BARCODE_CONTENT),
      Arguments.of(pdf417ReaderServiceMockException, multipartFileMockDefault, Boolean.TRUE, Boolean.FALSE, null)
    );
  }
}
