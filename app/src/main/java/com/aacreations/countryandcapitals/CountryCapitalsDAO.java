package com.aacreations.countryandcapitals;

import androidx.annotation.NonNull;

import java.util.List;

public interface CountryCapitalsDAO {
    void create(@NonNull CountryCapital toBeCreated, @Table.Tables int table);
    CountryCapital read(int id, @Table.Tables int table);
    void update(int id, @NonNull CountryCapital updateValue, @Table.Tables int table);
    void delete(int id, @Table.Tables int table);
}
