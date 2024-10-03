package org.example.library.dto;

public class RequestSignUpModel {
    private UserDto userDto;
    private String[] listInspiredBy;
    private String[] listTalent;
    private String[] genreBy;


    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public String[] getListInspiredBy() {
        return listInspiredBy;
    }

    public void setListInspiredBy(String[] listinspiredBy) {
        this.listInspiredBy = listinspiredBy;
    }

    public String[] getListTalent() {
        return listTalent;
    }

    public void setListTalent(String[] listTalent) {
        this.listTalent = listTalent;
    }

    public String[] getGenreBy() {
        return genreBy;
    }

    public void setGenreBy(String[] genreBy) {
        this.genreBy = genreBy;
    }
}
