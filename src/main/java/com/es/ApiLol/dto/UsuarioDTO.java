    package com.es.ApiLol.dto;

    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class UsuarioDTO {

        private String username;


        private String password;

        private String roles;


        public UsuarioDTO(String username, String password, String roles) {

            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public UsuarioDTO() {}
    }
