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
    #Test that a recept can not be stored without valid creds
    #Test that all the records for an account can be retreived

