package com.example.realserver_gradle.repository;



import com.example.realserver_gradle.entity.ResourceOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceOwnerRepository extends JpaRepository<ResourceOwner, Long> {

	public ResourceOwner findByUsername(String username);


}
