import io.strmprivacy.demo.proto.v1.ProtoExample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Proto {
    public static void main(String[] args) {
        try {
            var meetup = ProtoExample.Meetup.newBuilder()
                                            .setName("OpenValue Meetup 2022-04-13")
                                            .addAllSpeakers(List.of("Jan-Kees", "Robin"))
                                            .build();
            // Serialize to bytes
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            meetup.writeTo(output);
            byte[] bytes = output.toByteArray();
            System.out.println("output = " + Arrays.toString(bytes));

            // Deserialize to object
            ProtoExample.Meetup readBack = ProtoExample.Meetup.parseFrom(bytes);
            System.out.println("name: " + readBack.getName());
            System.out.println("speakers: " + readBack.getSpeakersList());

            // Compared with JSON
            var json = "{\"name\":\"OpenValue Meetup 2022-04-13\",\"speakers\":[\"Jan-Kees\",\"Robin\"]}";
            System.out.println("output = " + Arrays.toString(json.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
