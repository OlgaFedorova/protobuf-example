package ofedorova;


import com.google.protobuf.util.JsonFormat;
import ofedorova.models.UserProfile;
import ofedorova.models.UserProfiles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ProtobufExampleApplication {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Protobuf-example!");
        ProtobufExampleBuilder exampleBuilder = new ProtobufExampleBuilder();

        UserProfiles.Builder userProfilesBuilder = UserProfiles.newBuilder();
        for (int i = 1; i <= 10000; i++){
            userProfilesBuilder.addUserProfiles(exampleBuilder.buildUserProfile(i));
        }
        UserProfiles userProfiles = userProfilesBuilder.build();

        try(OutputStream outputStream = new FileOutputStream("user_profiles.bin")){
            userProfiles.writeTo(outputStream);
        }

        try(OutputStream outputStream = new FileOutputStream("user_profiles.json")){
            outputStream.write(JsonFormat.printer().print(userProfilesBuilder).getBytes());
        }

        System.out.println("---------------------------------------");
        System.out.println("Deserialize object:");

        try(InputStream inputStream = new FileInputStream("user_profiles.bin")) {
            System.out.println(UserProfile.parseFrom(inputStream));
        }
    }
}
