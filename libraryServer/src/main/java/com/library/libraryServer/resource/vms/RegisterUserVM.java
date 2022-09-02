package com.library.libraryServer.resource.vms;

    import com.library.libraryServer.domain.enums.*;
    import lombok.Data;
import javax.validation.constraints.Email;

    @Data
    public class RegisterUserVM {
        private String name;

        @Email
        private String email;
        private String password;

        @Data
        public static class Errorvm {
            private ApiStatus status;
            private String statusReason;
            private Integer code;//error code

            public Errorvm() {
            }

            public Errorvm(String statusReason, Integer code) {
                this.status=ApiStatus.FAILED;
                this.statusReason = statusReason;
                this.code = code;
            }

            public Errorvm(ApiStatus status, String statusReason, Integer code) {
                this.status = status;
                this.statusReason = statusReason;
                this.code = code;
            }
        }
    }


