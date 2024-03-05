package com.paper.dao.impl;

import com.paper.bean.Paper;
import com.paper.dao.BaseDao;
import com.paper.dao.PaginationDao;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaginationDaoImpl extends BaseDao implements PaginationDao {


    @Override
    public List<Paper> getPaperyList(HashMap<String, Object> params) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from t_paper where 1=1 ");

        ArrayList<Object> objects = new ArrayList<>();

        if (params.containsKey("title")) {
            sql.append(" and title like ?");
            objects.add("%" + params.get("title") + "%");
        }

        if (params.containsKey("type")) {
            sql.append(" and type = ?");
            objects.add(params.get("type"));
        }

        if (params.containsKey("start") && params.containsKey("pageSize")) {
            sql.append(" limit ?,?");
            objects.add(params.get("start"));
            objects.add(params.get("pageSize"));
        }

        resultSet = executeQuery(sql.toString(), objects.toArray());


        List<Paper> paperList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Paper paper = new Paper();
                paper.setId(resultSet.getInt("id"));
                paper.setTitle(resultSet.getString("title"));
                paper.setType(resultSet.getInt("type"));
                paper.setContent(resultSet.getString("content"));
                paper.setCrateBy(resultSet.getString("createBy"));
                paper.setFilename(resultSet.getString("filename"));
                paper.setCreateDate(resultSet.getDate("createDate"));
                paper.setModifyDate(resultSet.getDate("modifyDate"));

                paperList.add(paper);
            }

            return paperList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResource();
        }
    }

    @Override
    public int getDataTotal(HashMap<String, Object> params) {

        StringBuffer sql = new StringBuffer();
        sql.append("select count(*) from t_paper where 1=1 ");

        ArrayList<Object> objects = new ArrayList<>();
        if (params.containsKey("title")) {
            sql.append(" and title like ?");
            objects.add("%" + params.get("title") + "%");
        }

        if (params.containsKey("type")) {
            sql.append(" and type = ?");
            objects.add(params.get("type"));
        }

        resultSet = executeQuery(sql.toString(), objects.toArray());

        try {
            int sum = -1;
            while (resultSet.next()) {
                sum = resultSet.getInt(1);
            }

            return sum;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeResource();
        }
    }

    @Override
    public void addPaper(HashMap<String, Object> params) {

        StringBuffer sql = new StringBuffer();
        sql.append("insert into t_paper(title,type,content,filename,createDate,modifyDate) values(?,?,?,?,?,?)");

        ArrayList<Object> arrayList = new ArrayList<>();

        arrayList.add(params.get("title"));
        arrayList.add(params.get("type"));
        arrayList.add(params.get("content"));
        arrayList.add(params.get("filename"));
        arrayList.add(params.get("createDate"));
        arrayList.add(params.get("modifyDate"));

        super.executeUpdate(sql.toString(), arrayList.toArray());
    }

    @Override
    public void deletePaper(HashMap<String, Object> params) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from t_paper where id=?");

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(params.get("id"));

        executeUpdate(sql.toString(), arrayList.toArray());
    }

    @Override
    public Paper getPaperById(HashMap<String, Object> params) {

        StringBuffer sql = new StringBuffer();
        sql.append("select * from t_paper where id=?");
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(params.get("id"));
        resultSet = executeQuery(sql.toString(), arrayList.toArray());


        Paper paper = new Paper();
        try {
            while (resultSet.next()) {

                paper.setId(resultSet.getInt("id"));
                paper.setTitle(resultSet.getString("title"));
                paper.setType(resultSet.getInt("type"));
                paper.setContent(resultSet.getString("content"));
                paper.setCrateBy(resultSet.getString("crateBy"));
                paper.setFilename(resultSet.getString("filename"));
                        paper.setCreateDate(resultSet.getDate("createDate"));
                paper.setModifyDate(resultSet.getDate("modifyDate"));

            }
            return paper;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePaper(HashMap<String, Object> params) {
        StringBuffer sql = new StringBuffer();
        sql.append("update t_paper set title = ? ,type = ? ,content = ?,filename = ?,modifyDate=? where id =?");

        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(params.get("title"));
        arrayList.add(params.get("type"));
        arrayList.add(params.get("content"));
        arrayList.add(params.get("filename"));
        arrayList.add(params.get("modifyDate"));
        arrayList.add(params.get("id"));
        super.executeUpdate(sql.toString(), arrayList.toArray());

    }

    @Override
    public Paper getPaperByTitle(HashMap<String, Object> params) {

        String sql = "select title from t_paper where title = ?";

        ArrayList<Object> arrayList = new ArrayList<>();

        if (params.containsKey("title")) {
            arrayList.add(((String) params.get("title")));

            resultSet = super.executeQuery(sql, arrayList.toArray());

            Paper paper = new Paper();
            try {

                boolean isNext = resultSet.next();

                if (isNext) {
                    while (isNext) {
                        String title = resultSet.getString("title");
                        paper.setTitle(title);
                    }
                } else {
                    paper.setTitle("");
                }
                return paper;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
