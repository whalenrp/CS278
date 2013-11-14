"""
This file is for the unit testing of the python component of the Mobile Receipts
project.
"""
from db_interface import AccountsWrapper
from db_interface import Receipt

class Test_Suite:
    def test_trivial(self):
        assert 'a'=='a'
    
    def setUp(self):
        self.a = AccountsWrapper()
        self.a.users.insert({"username":"test_user","password":"test_pass"})
        
        self.r = Receipt()
        self.a.receipts.insert({"username":"test_user","receipt":self.r.serialize()})
    
    
    def tearDown(self):
        self.a.users.remove({"username":"test_user"})
        self.a.receipts.remove({"username":"test_user"})
    
    
    #Test that an account can be added
    def test_add_account(self):
        self.a.add_account("new_user","new_pass")
        assert self.a.users.find({"username":"new_user",
                    "password":"new_pass"}).count() == 1
        self.a.users.remove({"username":"new_user","password":"new_pass"})
        assert self.a.users.find({"username":"new_user",
                    "password":"new_pass"}).count() == 0
	
    #Test that an account can only be accessed with the right creds
    def test_retrieve_authorized_data(self):
        assert len(self.a.retrieve_all_receipts("fake_user","fake_password")) == 0 
        assert len(self.a.retrieve_all_receipts("test_user","fake_password")) == 0
        assert len(self.a.retrieve_all_receipts("test_user","test_pass")) == 1
    
    #Test that a receipt can be stored (with creds)
    def test_store_receipt(self):
        test_r = Receipt("12/12/12","Walmart",10.0)
        assert self.a.add_receipt("test_user","test_pass",test_r) == True
        records = self.a.receipts.find({"username":"test_user",
                                        "receipt":{"date":"12/12/12",
                                                    "store":"Walmart",
                                                    "total":10}})
        assert records.count() == 1
        assert Receipt(json=records[0]["receipt"]) == test_r
        self.a.receipts.remove({"username":"test_user","receipt.date":"12/12/12",
                                "receipt.store":"Walmart","receipt.total":10})
    
    #Test that a recept can not be stored without valid creds
    def test_store_unauthorized_receipt(self):
        test_r = Receipt("this","won't work",0)
        num_receipts = self.a.receipts.find().count()
        assert self.a.add_receipt("test_user","fake_pass",test_r) == False
        assert self.a.add_receipt("fake_user","fake_pass",test_r) == False
        assert num_receipts == self.a.receipts.find().count()
    
    #Test receipt serialization
    def test_serialization(self):
        serial = {"date":"1/1/2013","store":"Target","total":0.0}
        assert self.r.serialize() == serial
    
    #Test receipt equality
    def test_receipt_equality(self):
       r1 = Receipt("1/1/2013","Target",0.0)
       r2 = Receipt(json={"date":"1/1/2013","store":"Target","total":0.0})
       assert r1 == r2
       assert r2 == r1
       assert r1 == self.r
       assert r2 == self.r
       r1.store = "Walmart"
       assert not r1 == r2
       assert not r1 == self.r
       r2.total = 1.0
       assert not r2 == self.r
