package presenter;

import java.util.List;

public interface IPersonaView {

    String getNume();
    String getPrenume();
    String getAnNastere();
    String getNationalitate();
    String getIdSelectat();


    void setNume(String nume);
    void setPrenume(String prenume);
    void setAnNastere(String an);
    void setNationalitate(String nat);
    void setIdSelectat(String id);


    void afiseazaEntitati(List<String[]> randuri);
    void afiseazaMesaj(String mesaj);
    void afiseazaEroare(String eroare);
    void curataFormular();
}