/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jdbcormmysqlswing.metier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Langenoir
 */
public class MetierCatalogueImpl implements IMetier{

    @Override
    public void addCategorie(Categorie c) {
        Connection con =SingletonConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("insert into categories(nom_categorie) values(?)");
            ps.setString(1,c.getNomCat());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }

    @Override
    public void addProduit(Produit p, int idCat) {
    Connection con =SingletonConnection.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement("insert into produits values(?,?,?,?,?)");
            ps.setString(1,p.getRefProduit());
            ps.setString(2,p.getNomProduit());
            ps.setDouble(3,p.getPrix());
            ps.setInt(4,p.getQuantite());
            ps.setInt(5,idCat);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }           }

    @Override
    public List<Produit> getProduitParMC(String mc) {
        Connection con =SingletonConnection.getConnection();
        List<Produit> prods = new ArrayList<Produit>(); 
        try {
            PreparedStatement ps = con.prepareStatement("select * from produits where nom_produit like ?");
            ps.setString(1,"%"+mc+"%");
            ResultSet rs = ps.executeQuery();
            //a chaque fois que le resultSet  existe alors on passe au suivant
            //on recupere les produits
            while(rs.next()){                
                Produit p = new Produit();
                p.setRefProduit(rs.getString("ref_produit"));
                p.setNomProduit(rs.getString("nom_produit"));
                p.setPrix(rs.getDouble("prix"));
                p.setQuantite(rs.getInt("quantite"));
                int idCat = rs.getInt("id_cat");                
                //le preparestatement qui va parcourir la table categorie et conllectionner l'id des 
                //divers categories
                PreparedStatement ps2 = con.prepareStatement("select * from categories where id_cat = ?");
                ps2.setInt(1, idCat);
                ResultSet rs2 = ps2.executeQuery();
                
                //a chaque fois que le resultSet 2 existe alors on passe au suivant
                if(rs2.next()){
                Categorie cat = new Categorie();
                cat.setIdCat(rs2.getInt("id_cat"));
                cat.setNomCat(rs2.getString("nom_categorie"));                
                //a chaque fois qu'on trouve une categorie on l'ajoute au produit au quel il correspond
                p.setCategorie(cat);
                ps2.close();
                }
                //ajout du produit a la liste des produits cree au debut de la methode
                prods.add(p);
            }
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prods;
    }

    @Override
    public List<Produit> getProduitParCat(int idCat) {
        Connection con =SingletonConnection.getConnection();
        List<Produit> prods = new ArrayList<Produit>(); 
        try {
            PreparedStatement ps = con.prepareStatement("select * from produits where id_cat = ?");
            ps.setInt(1,idCat);
            ResultSet rs = ps.executeQuery();
            //a chaque fois que le resultSet  existe alors on passe au suivant
            //on recupere les produits
            while(rs.next()){                
                Produit p = new Produit();
                p.setRefProduit(rs.getString("ref_produit"));
                p.setNomProduit(rs.getString("nom_produit"));
                p.setPrix(rs.getDouble("prix"));
                p.setQuantite(rs.getInt("quantite"));                
                //le preparestatement qui va parcourir la table categorie et conllectionner l'id des 
                //divers categories
                PreparedStatement ps2 = con.prepareStatement("select * from categories where id_cat = ?");
                ps2.setInt(1, idCat);
                ResultSet rs2 = ps2.executeQuery();                
                //a chaque fois que le resultSet 2 existe alors on passe au suivant
                if(rs2.next()){
                Categorie cat = new Categorie();
                cat.setIdCat(rs2.getInt("id_cat"));
                cat.setNomCat(rs2.getString("nom_categorie"));                
                //a chaque fois qu'on trouve une categorie on l'ajoute au produit au quel il correspond
                p.setCategorie(cat);
                 ps2.close();
                               }
                
                //ajout du produit a la liste des produits cree au debut de la methode
                prods.add(p);
            }
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return prods;    }

    @Override
    public List<Categorie> getAllCategorie() {
        Connection con =SingletonConnection.getConnection();
        List<Categorie> cats = new ArrayList<Categorie>(); 
        try {
            PreparedStatement ps = con.prepareStatement("select * from categories ");
            ResultSet rs = ps.executeQuery();
            //a chaque fois que le resultSet  existe alors on passe au suivant
            //on recupere les produits
            while(rs.next()){                
                Categorie c = new Categorie();
                c.setIdCat(rs.getInt("id_cat"));
                c.setNomCat(rs.getString("nom_categorie"));
                cats.add(c);
            }
               
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cats; 
    }

    @Override
    public Categorie getCategorie(int idCat) {
        Connection con =SingletonConnection.getConnection();
        Categorie cat = null; 
        try {
            PreparedStatement ps = con.prepareStatement("select * from categories where id_cat =?");
            ps.setInt(1, idCat);
            ResultSet rs = ps.executeQuery();
            //a chaque fois que le resultSet  existe alors on passe au suivant
            //on recupere les produits
            if(rs.next()){                
               cat = new Categorie();
               cat.setIdCat(rs.getInt("id_cat"));
               cat.setNomCat(rs.getString("nom_categorie"));
               List<Produit> prods = this.getProduitParCat(idCat);
               cat.setProduits(prods);
                 }
               
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cat;
    }
    
    
}
