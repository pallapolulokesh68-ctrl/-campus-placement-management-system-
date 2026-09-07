package com.compus.placementmanagement;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company addCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public Company updateCompany(Long id, Company company) {

        Company existingCompany =
                companyRepository.findById(id).orElse(null);

        if (existingCompany == null) {
            return null;
        }

        existingCompany.setName(company.getName());
        existingCompany.setEmail(company.getEmail());
        existingCompany.setPassword(company.getPassword());

        return companyRepository.save(existingCompany);
    }

    public String deleteCompany(Long id) {

        companyRepository.deleteById(id);

        return "Company deleted successfully";
    }
}