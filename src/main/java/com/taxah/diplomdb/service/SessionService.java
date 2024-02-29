package com.taxah.diplomdb.service;

import com.taxah.diplomdb.model.*;

import com.taxah.diplomdb.model.abstractClasses.Account;
import com.taxah.diplomdb.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class SessionService {

    //    private ProductUsingUserRepository productUsingUserRepository;
    private SessionRepository sessionRepository;
    private UserRepository userRepository;
    private PayFactRepository payFactRepository;
    private CheckRepository checkRepository;
    private ProductUsingRepository productUsingRepository;
    private TempUserRepository tempUserRepository;

    public Long createSessionAndMembers(List<TempUser> tempMembers, Long admin) {
        Session session = sessionRepository.save(new Session());
        Long sessionId = session.getId();
        List<TempUser> tempMemberList = new ArrayList<>();
        for (Account a : tempMembers) {
            TempUser tempUser = tempUserRepository.save(new TempUser());
            tempUser.setFirstname(a.getFirstname());
            tempUser.setLastname(a.getLastname());
            tempUser.setSessionId(sessionId);
            tempMemberList.add(tempUser);
        }
        session.setMembersList(tempMemberList);
        session.setAdminId(admin);
        List<PayFact> payFacts = new ArrayList<>();
        session.setPayFact(payFacts);
        session = sessionRepository.save(session);
        return session.getId();
    }


    public List<PayFact> addPayFact(Long userId, Double amount, Long sessionId) {
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        Optional<TempUser> optionalTempUser = tempUserRepository.findById(userId);

        Session session = null;
        TempUser user;
        if (optionalSession.isPresent() && optionalTempUser.isPresent()) {
            session = optionalSession.get();
            user = optionalTempUser.get();
            PayFact payFact = payFactRepository.save(new PayFact());
            payFact.setSession(session);
            payFact.setUserData(user.getFirstname() + " " + user.getLastname());
            payFact.setAmount(amount);
            payFact.setUserId(user.getId());
            session.addPayFact(payFact);
            payFactRepository.save(payFact);
        }
        if (session != null){
            return session.getPayFact();
        }
        return null;
    }

    public Long createCheck(Long sessionId, String name){
        Check check = checkRepository.save(new Check());
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if (optionalSession.isPresent()) {
            check.setSession(optionalSession.get());
            check.setName(name);
            checkRepository.save(check);
            return check.getId();
        }
        return null;
    }

    public List<ProductUsing> addProductUsing(Long checkId, String productName, Double cost, List<TempUser> tempUsers){
        Optional<Check> optionalCheck = checkRepository.findById(checkId);
        ProductUsing productUsing = productUsingRepository.save(new ProductUsing());

        if (optionalCheck.isPresent()){
            Check check = optionalCheck.get();
            productUsing.setCheck(check);
            productUsing.setProductName(productName);
            productUsing.setCost(cost);
            productUsing.setUsers(tempUsers);
            check.addProductUsing(productUsing);
            checkRepository.save(check);
            return check.getProductUsingList();
        }
        return null;
    }

//    public Session writeSession2(Session session, Long admin) {
//        Session session1 = sessionRepository.save(new Session());
//        Long sessionId = session1.getId();
////        session.setMembersList(null); //TODO доделать memberList и убрать
//        List<TempUser> tempUserList=new ArrayList<>();
//        for (Account u : session.getMembersList()){
//            TempUser tempUser = new TempUser(sessionId,u.getFirstname(), u.getLastname());
//            tempUserList.add(tempUser);
//        }
//        System.out.println(tempUserRepository.saveAll(tempUserList));
//
//        session.setId(session1.getId());
//
//        session1.setAdminId(admin);
//        System.out.println("session members = " + session.getMembersList());
//        System.out.println("session id = " + sessionId);
//
//        for (PayFact pf: session.getPayFact()){
//            //TODO нужно переделать чтобы id приходили из TempUser и класс тоже
//            PayFact payFact = payFactRepository.save(new PayFact());
//            pf.setId(payFact.getId());
//            pf.setSession(session);
//            System.out.println("Payfact id = " + payFact.getId() +" "+ pf);
////            payFactRepository.save(pf);
//        }
//        List<Check> checks = session.getCheckList();
//        for (Check c : checks){
//            Check check = checkRepository.save(new Check());
//            c.setId(check.getId());
//            c.setSession(session);
//            System.out.println("Check id = " + check.getId()+" "+c);
//            for (ProductUsing p : c.getProductUsingList()){
//                System.out.println("Start");
//                ProductUsing productUsing = productUsingRepository.save(new ProductUsing());
//                p.setId(productUsing.getId());
//                p.setCheck(c);
//                System.out.println("ProductUsing id = " + productUsing.getId()+" "+p);
//                System.out.println("End");
//            }
//        }
//        //TODO НА ЗАВТРА СДЕЛАТЬ ОБНОВЛЕНИЕ СЕССИИ ЮЗЕРОВ ИЗ JSON
////        checkRepository.saveAll(checks);
//
//        System.out.println("Session = " + session);
//        Session sessionToSave = sessionRepository.saveAndFlush(session);
//        return sessionToSave;
//    }


    public Session getSession(Long id) {
        Optional<Session> optional = sessionRepository.findById(id);
        return optional.orElse(null);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Long myId(Long id) {
        Optional<User> optional = userRepository.findById(Math.toIntExact(id));
        return optional.map(User::getId).orElse(null);
    }
}

//public Session writeSession(Session session, Long admin) {
//    Session session1 = sessionRepository.save(new Session());
//    Long sessionId = session1.getId();
////        session.setMembersList(null); //TODO доделать memberList и убрать
//    List<TempUser> tempUserList=new ArrayList<>();
//    for (Account u : session.getMembersList()){
//        TempUser tempUser = new TempUser(sessionId,u.getFirstname(), u.getLastname());
//        tempUser.setFirstname(u.getFirstname());
//        tempUser.setLastname(u.getLastname());
//        tempUserList.add(tempUser);
//    }
//    tempUserRepository.saveAll(tempUserList);
//    session.setMembersList(tempUserList);
//
//    session1.setAdminId(admin);
//    System.out.println("session members = " + session.getMembersList());
//    System.out.println("session id = " + sessionId);
//    List<PayFact> payFactList = new ArrayList<>();
//    for (PayFact pf: session.getPayFact()){

//        PayFact payFact = payFactRepository.save(new PayFact());
//        payFact.setUserData(pf.getUserData());
//        payFact.setUserId(pf.getUserId());
//        payFact.setAmount(pf.getAmount());
//        payFact.setSession(session1);
//        System.out.println("Payfact id = " + payFact.getId() +" "+ payFact);
////            payFactRepository.save(pf);
//        payFactList.add(payFact);
//    }
//    session1.setPayFact(payFactList);
////        payFactRepository.saveAll(payFactList);
//
//    List<Check> checkList = new ArrayList<>();
//    for (Check c : session.getCheckList()){
//        Check check = checkRepository.save(new Check());
//        check.setName(c.getName());
//        check.setSession(session1);
//        System.out.println("Check id = " + check.getId()+" "+check);
//
//        List<ProductUsing> productUsingList = new ArrayList<>();
//        for (ProductUsing p : c.getProductUsingList()){
//            ProductUsing productUsing = productUsingRepository.save(new ProductUsing());
//            productUsing.setCheck(check);
//            productUsing.setProductName(p.getProductName());
//            productUsing.setCost(p.getCost());
//
//            List<TempUser> tempUsers = new ArrayList<>();
//            for (Account tu: p.getUsers()){
//                for (TempUser tempUser :tempUserList){
//                    if (tempUser.getFirstname().equals(tu.getFirstname()) &&
//                            tempUser.getLastname().equals(tu.getLastname())){
//                        tempUser.setProductUsingList(c.getProductUsingList());
//                        tempUsers.add(tempUser);
//                        break;
//                    }
//                }
//            }
//            productUsing.setUsers(tempUsers);
//            System.out.println("        ProductUsing id = " + productUsing.getId()+" "+productUsing);
//            productUsingList.add(productUsing);
//        }
//        check.setProductUsingList(productUsingList);
//        System.out.println("NEW Check " + check);
//        checkList.add(check);
//    }
//
//    System.out.println("NEW CHECK LIST " + checkList);
//    session1.setCheckList(checkList);
////        productUsingRepository.saveAll(productUsingList);
//    System.out.println("Session = " + session1);
//    return sessionRepository.save(session1);
//}

//private List<TempUser> comparedUsersListReturn(List<Account> p, List<TempUser> tempUserList){
//    List<TempUser> tempUsers = new ArrayList<>();
//    for (Account tu: p){
//        for (TempUser tempUser :tempUserList){
//            if (tempUser.getFirstname().equals(tu.getFirstname()) &&
//                    tempUser.getLastname().equals(tu.getLastname())){
//                tempUsers.add(tempUser);
//                break;
//            }
//        }
//    }
//    return tempUsers;
//}

