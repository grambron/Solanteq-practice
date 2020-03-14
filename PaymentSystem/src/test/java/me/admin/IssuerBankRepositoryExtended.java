package me.admin;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class IssuerBankRepositoryExtended implements IssuerBankRepository {

    private List<IssuerBank> dataBase;

    IssuerBankRepositoryExtended() {
        dataBase = new ArrayList<>();
    }

    public void setUpTable(List<IssuerBank> base) {
        dataBase = base;
    }

    @Override
    public IssuerBank findByBin(String bin) {
        return dataBase
                .stream()
                .filter(entry -> entry.getBin().equals(bin))
                .findFirst().orElse(null);
    }

    @Override
    public List<IssuerBank> findAll() {
        return new ArrayList<>(dataBase);
    }

    @Override
    public List<IssuerBank> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<IssuerBank> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<IssuerBank> findAllById(Iterable<Integer> iterable) {
        return null;
    }

    @Override
    public long count() {
        return dataBase.size();
    }

    @Override
    public void deleteById(Integer integer) {
    }

    @Override
    public void delete(IssuerBank issuerBank) {
        dataBase.remove(issuerBank);
    }

    @Override
    public void deleteAll(Iterable<? extends IssuerBank> iterable) {
    }

    @Override
    public void deleteAll() {
    }

    @Override
    public <S extends IssuerBank> S save(S s) {
        dataBase.add(s);
        return s;
    }

    @Override
    public <S extends IssuerBank> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<IssuerBank> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {
    }

    @Override
    public <S extends IssuerBank> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<IssuerBank> iterable) {
    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public IssuerBank getOne(Integer integer) {
        return null;
    }

    @Override
    public <S extends IssuerBank> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends IssuerBank> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends IssuerBank> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends IssuerBank> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends IssuerBank> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends IssuerBank> boolean exists(Example<S> example) {
        return false;
    }
}
