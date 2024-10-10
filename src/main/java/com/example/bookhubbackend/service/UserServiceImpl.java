package com.example.bookhubbackend.service;

import com.example.bookhubbackend.model.User;
import com.example.bookhubbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void applyMileage(String userId, int useMileage, int earnMileage) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int currentMileage = user.getMileage();
            int newMileage = currentMileage - useMileage + earnMileage;

            // 마일리지가 음수가 되지 않도록 처리
            if (newMileage < 0) {
                newMileage = 0;
            }

            user.setMileage(newMileage);
            userRepository.save(user);

            // 디버깅을 위한 로그 추가
            System.out.println("User ID: " + userId);
            System.out.println("Current Mileage: " + currentMileage);
            System.out.println("Use Mileage: " + useMileage);
            System.out.println("Earn Mileage: " + earnMileage);
            System.out.println("Updated Mileage: " + newMileage);
        }
    }

    @Override
    public int getUserMileage(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            return userOptional.get().getMileage();
        }
        return 0; // 사용자 미존재 시 기본값
    }

    @Override
    public void updateUserMileage(String userId, int mileageChange) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            int newMileage = user.getMileage(); // 기존 마일리지에 변경값을 더함
            user.setMileage(newMileage);
            userRepository.save(user); // 마일리지 업데이트
        }
    }

}
