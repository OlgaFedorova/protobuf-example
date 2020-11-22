package ofedorova;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import ofedorova.models.PhoneNumber;
import ofedorova.models.PhoneType;
import ofedorova.models.Profession;
import ofedorova.models.UserProfile;

import java.time.LocalDateTime;
import java.util.List;

public class ProtobufExampleBuilder {

    public UserProfile buildUserProfile(int index) {
        UserProfile.Builder userProfileBuilder = UserProfile.newBuilder();
        UserProfile userProfile = userProfileBuilder
                .setId(index)
                .setFirstName("FirstName" + index)
                .setLastName("LastName" + index)
                .setEmail("test" + index + "@email.com")
                .addAllPhones(List.of(PhoneNumber.newBuilder()
                                .setNumber("11111111111")
                                .setType(PhoneType.HOME)
                                .build(),
                        PhoneNumber.newBuilder()
                                .setNumber("2222222222")
                                .setType(PhoneType.MOBILE)
                                .build()))
                .setHeight(160 + (index % 10))
                .setWeight(50 + (index % 10))
                .setAge(18 + (index % 10))
                .setIsActive(true)
                .setPhoto(ByteString.EMPTY)
                .setDetails(Any.newBuilder().setValue(ByteString.EMPTY).build())
                .setAddress1("Address" + index)
                .putProfessions("first", Profession.newBuilder().setTitle("waiter").build())
                .putProfessions("second", Profession.newBuilder().setTitle("doctor").build())
                .setCreatedDate(Timestamp.newBuilder().setSeconds(LocalDateTime.now().getSecond()).build())
                .build();

        return userProfile;
    }
}
