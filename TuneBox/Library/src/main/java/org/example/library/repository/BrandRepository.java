package org.example.library.repository;

import org.example.library.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    //    Search by keyword
    @Query("select b from Brand b where b.name like %?1%")
    public List<Brand> findByKeyword(String keyword);

    // List brand A
    @Query("select b from Brand b where b.name like 'A%' or b.name like 'a%'")
    public List<Brand> findByNameStartingWithA();

    // List brand B
    @Query("select b from Brand b where b.name like 'B%' or b.name like 'b%'")
    public List<Brand> findByNameStartingWithB();

    // List brand C
    @Query("select b from Brand b where b.name like 'C%' or b.name like 'c%'")
    public List<Brand> findByNameStartingWithC();

    // List brand D
    @Query("select b from Brand b where b.name like 'D%' or b.name like 'd%'")
    public List<Brand> findByNameStartingWithD();

    // List brand E
    @Query("select b from Brand b where b.name like 'E%' or b.name like 'e%'")
    public List<Brand> findByNameStartingWithE();

    // List brand F
    @Query("select b from Brand b where b.name like 'F%' or b.name like 'f%'")
    public List<Brand> findByNameStartingWithF();

    // List brand G
    @Query("select b from Brand b where b.name like 'G%' or b.name like 'g%'")
    public List<Brand> findByNameStartingWithG();

    // List brand H
    @Query("select b from Brand b where b.name like 'H%' or b.name like 'h%'")
    public List<Brand> findByNameStartingWithH();

    // List brand I
    @Query("select b from Brand b where b.name like 'I%' or b.name like 'i%'")
    public List<Brand> findByNameStartingWithI();

    // List brand J
    @Query("select b from Brand b where b.name like 'J%' or b.name like 'j%'")
    public List<Brand> findByNameStartingWithJ();

    // List brand K
    @Query("select b from Brand b where b.name like 'K%' or b.name like 'k%'")
    public List<Brand> findByNameStartingWithK();

    // List brand L
    @Query("select b from Brand b where b.name like 'L%' or b.name like 'l%'")
    public List<Brand> findByNameStartingWithL();

    // List brand M
    @Query("select b from Brand b where b.name like 'M%' or b.name like 'm%'")
    public List<Brand> findByNameStartingWithM();

    // List brand N
    @Query("select b from Brand b where b.name like 'N%' or b.name like 'n%'")
    public List<Brand> findByNameStartingWithN();

    // List brand O
    @Query("select b from Brand b where b.name like 'O%' or b.name like 'o%'")
    public List<Brand> findByNameStartingWithO();

    // List brand P
    @Query("select b from Brand b where b.name like 'P%' or b.name like 'p%'")
    public List<Brand> findByNameStartingWithP();

    // List brand Q
    @Query("select b from Brand b where b.name like 'Q%' or b.name like 'q%'")
    public List<Brand> findByNameStartingWithQ();

    // List brand R
    @Query("select b from Brand b where b.name like 'R%' or b.name like 'r%'")
    public List<Brand> findByNameStartingWithR();

    // List brand S
    @Query("select b from Brand b where b.name like 'S%' or b.name like 's%'")
    public List<Brand> findByNameStartingWithS();

    // List brand T
    @Query("select b from Brand b where b.name like 'T%' or b.name like 't%'")
    public List<Brand> findByNameStartingWithT();

    // List brand U
    @Query("select b from Brand b where b.name like 'U%' or b.name like 'u%'")
    public List<Brand> findByNameStartingWithU();

    // List brand V
    @Query("select b from Brand b where b.name like 'V%' or b.name like 'v%'")
    public List<Brand> findByNameStartingWithV();

    // List brand W
    @Query("select b from Brand b where b.name like 'W%' or b.name like 'w%'")
    public List<Brand> findByNameStartingWithW();

    // List brand X
    @Query("select b from Brand b where b.name like 'X%' or b.name like 'x%'")
    public List<Brand> findByNameStartingWithX();

    // List brand Y
    @Query("select b from Brand b where b.name like 'Y%' or b.name like 'y%'")
    public List<Brand> findByNameStartingWithY();

    // List brand Z
    @Query("select b from Brand b where b.name like 'Z%' or b.name like 'z%'")
    public List<Brand> findByNameStartingWithZ();
}
