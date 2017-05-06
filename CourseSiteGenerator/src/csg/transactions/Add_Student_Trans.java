/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import csg.data.TAData;
import csg.data.student;
import jtps.jTPS_Transaction;

/**
 *
 * @author Neil
 */
public class Add_Student_Trans implements jTPS_Transaction {
    student stu;
    TAData data;
    public Add_Student_Trans(student stu, TAData data){
        this.stu = stu;
        this.data = data;
    }
    @Override
    public void doTransaction() {
        data.addStudent(stu.getFirstName(), stu.getLastName(), stu.getTeam(), stu.getRole());
        stu = data.getStudent(stu.getFirstName(), stu.getLastName(), stu.getTeam());
    }
    @Override
    public void undoTransaction() {
        data.deleteStudent(stu);
    }
}
