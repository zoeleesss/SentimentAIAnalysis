# -*- coding: utf-8 -*-
from __future__ import print_function, unicode_literals
import  xdrlib ,sys
from xlutils.copy import copy
import os
import xlrd

import json
import requests


import xlwt

global index
index=0

def getListFiles(path):
    ret=[]
    for root,dirs,files in os.walk(path):
        for filespath in files:
            ret.append(os.path.join(root,filespath))
    return ret



def open_excel(file= 'file.xls'):
    try:
        data = xlrd.open_workbook(file)
        return data
    except:
        print("xls open error")
#根据索引获取Excel表格中的数据   参数:file：Excel文件路径     colnameindex：表头列名所在行的所以  ，by_index：表的索引
def excel_table_byindex(file= 'file.xls',colnameindex=0,by_index=0):
    global  index

    print("index: "+str(index))
    data = open_excel(file)
    table = data.sheets()[by_index]
    nrows = table.nrows #行数
    ncols = table.ncols #列数
    colnames =  table.row_values(colnameindex) #某一行数据
    list =[]

    print(table.cell(1, 0).value)

    for rownum in range(1,nrows):

         row = table.row_values(rownum)


         try:


            print(table.cell(rownum,2).value)
         except:
            print('err')
             #table.put_cell(1,1,2,999,0)
             #for i in range(len(colnames)):


    #return list

#根据名称获取Excel表格中的数据   参数:file：Excel文件路径     colnameindex：表头列名所在行的所以  ，by_name：Sheet1名称
def excel_table_byname(file= 'file.xls',colnameindex=0,by_name=u'Sheet1'):
    data = open_excel(file)
    table = data.sheet_by_name(by_name)
    nrows = table.nrows #行数
    colnames =  table.row_values(colnameindex) #某一行数据
    list =[]
    for rownum in range(1,nrows):
         row = table.row_values(rownum)
         if row:
             app = {}
             for i in range(len(colnames)):
                app[colnames[i]] = row[i]
             list.append(app)
    return list


def update_excel(file='copy2.xls'):



    global index
    data = xlrd.open_workbook(file)
    table=data.sheets()[0]
    nrows = table.nrows  # 行数
    ncols = table.ncols  # 列数

    SENTIMENT_URL = 'http://api.bosonnlp.com/sentiment/analysis'
    # 注意：在测试时请更换为您的API Token
    #headers =
    #headers =










    new_data=2
    w = copy(data)
    ws = w.get_sheet(0)
    #
    headers_list=[
                  {'X-Token': 'oOPS3YQG.15186.14TwOCy5W09A'},
                  {'X-Token': 'xxx'},
                  {'X-Token': 'xxx'},
                  {'X-Token': 'xxx'},
                  {'X-Token': 'xxx'},
                  {'X-Token': 'xxx'},
                  {'X-Token': 'xxx'}]


    for rownum in range(0,nrows):
        print(table.cell(rownum, 0).value)
        print(table.cell(rownum, 3).value)
        s = []
        s.append(table.cell(rownum, 3).value)


        data = json.dumps(s)

        try:
            resp = requests.post(SENTIMENT_URL, headers=headers_list[index], data=data.encode('utf-8'))
            origin_text=resp.text
            ss = origin_text.split(',')
            ss1 = ss[0]
            sss1 = ss1.split("[[")

            ss2 = ss[1]
            sss2 = ss2.split("]]")

            # process data : 0 , 1 -> -5 , 5

            print(resp.text)
            processed_text = float(sss1[1]) * 5 - float(sss2[0]) * 5
            print("origin_text: " + str(origin_text) + "  processed_text: " + str(processed_text))
            new_data = processed_text

            print("old_data:" + str(table.cell(rownum, 1).value) + " new_data:" + str(new_data))
            ws.write(rownum, 1, new_data)
            print("************divding line************")
        except:
            print("old _index: "+str(index) )
            index=index+1
            resp = requests.post(SENTIMENT_URL, headers=headers_list[index], data=data.encode('utf-8'))
            print( " new index :" + str(index ))
            origin_text = resp.text
            ss = origin_text.split(',')
            ss1 = ss[0]
            sss1 = ss1.split("[[")

            ss2 = ss[1]
            sss2 = ss2.split("]]")

            # process data : 0 , 1 -> -5 , 5

            print(resp.text)
            processed_text = float(sss1[1]) * 5 - float(sss2[0]) * 5
            print("origin_text: " + str(origin_text) + "  processed_text: " + str(processed_text))
            new_data = processed_text

            print("old_data:" + str(table.cell(rownum, 1).value) + " new_data:" + str(new_data))
            ws.write(rownum, 1, new_data)
            print("************divding line************")

#步骤1：给所有的SENTIMENT 编号序号
#步骤2：处理数据，并








    w.save(file)

def main():
#tables = \
   #excel_table_byindex('copy2.xls')

   #update_excel('大作业第1次_李业芃F.xls')
#   for row in tables:
#       print(row)

    global index

    ret=getListFiles("./")
    for each in ret:
        filepath=each.split("./")
        file_extension=filepath[1].split(".")[-1]
        #print(file_extension)
        if (file_extension!="xml" and file_extension!="py" and file_extension!="iml" and filepath[1]!="copy2.xls" and filepath[1]!='.DS_Store' ):
            print("--------------------------------")
            print("processing: "+filepath[1])
            update_excel(filepath[1])
            print("--------------------------------")



#   tables = excel_table_byname('copy2.xls')
#   for row in tables:
#       print(row)





if __name__=="__main__":
    main()

