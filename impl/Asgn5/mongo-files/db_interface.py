import pymongo
from pymongo import MongoClient

#guest_user = {"username":"guest", "password":"guest"}

class AccountsWrapper():
    """
    This class will be a wrapper for the pymongo internals of managing 
    accounts and their corresponding data from the MobileReceipts project.
    """
    def __init__(self):
        self.client = MongoClient()
        self.db = self.client.test
        self.users = self.db.users
        self.receipts = self.db.receipts
    
    def _are_valid_credentials(self, username, password):
        """
        Check that the username and password exist in the database. If 
        they don't, return 0. If more than 1 exists, returns -1. If 
        exactly 1, return 1.
        """
        result = self.users.find({"username":username,"password":password}).count()
        if result == 1:
            return 1
        elif result > 1:
            return -1
        else: #doesn't exist
            return 0
    
    def add_account(self, username, password):
        """
        If the account doesn't exist already, add one with the given username
        and password.
        """
        valid = self._are_valid_credentials(username, password)
        if valid == 0:    
            self.users.insert({"username":username,"password":password})
            return True
        else:
            return False
        
    
    def retrieve_all_receipts(self,username,password):
        if  self._are_valid_credentials(username,password) == 1:
            results = self.receipts.find({"username":username})
            all_receipts = []
            for match in results:
                all_receipts.append(match["receipt"])
            return all_receipts
        else:
            return []
        
    


class Receipt():
    
    def __init__(self, date="1/1/2013", store="Target", total=0.00):
        self.date = date
        self.store = store
        self.total = total

    def serialize(self):
        return {"date":self.date,"store":self.store,"total":self.total}

