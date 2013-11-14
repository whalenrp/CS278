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
        matching_users = self.users.find({"username":username}).count()
        result = self.users.find({"username":username,"password":password}).count()
        if matching_users == 1 and result == 1:
            return 1
        elif matching_users > 1:
            return -1
        else: #doesn't exist
            return 0
    
    def add_account(self, username, password):
        """
        If the account doesn't exist already, add one with the given username
        and password. Return True if successfully inserted, False otherwise.
        """
        valid = self._are_valid_credentials(username, password)
        if valid == 0:    
            self.users.insert({"username":username,"password":password})
            return True
        else:
            return False
        
    
    def retrieve_all_receipts(self,username,password):
        """
        This function checks to see if the username and password are valid.
        If they are, all the receipts associated with the username are retreived.
        A list of matching Receipt objects is returned.
        """
        if  self._are_valid_credentials(username,password) == 1:
            results = self.receipts.find({"username":username})
            all_receipts = []
            for match in results:
                all_receipts.append(Receipt(json=match["receipt"]))
            return all_receipts
        else:
            return []
        
    
    def add_receipt(self, username, password, receipt):
        """
        This function checks that the username and password match, and if they do,
        the serialized receipt is added to the database. Returns True if the record
        is added, False otherwise
        """
        if self._are_valid_credentials(username,password) == 1:
            self.receipts.insert({"username":username,"receipt":receipt.serialize()})
            return True
        else:
            return False


class Receipt():
    
    def __init__(self, date="1/1/2013", store="Target", total=0.00 ,json=None):
        """
        Can construct self based off of individual parameters or off of a 
        json/dict structure
        """
        if json==None:
            self.date = date
            self.store = store
            self.total = total
        else:
            self.date = json["date"]
            self.store = json["store"]
            self.total = json["total"]
        
    def __eq__(self,other):
        d = self.date == other.date
        s = self.store == other.store
        t = self.total == other.total
        return d and s and t
    
    def serialize(self):
        """
        returns a dictionary representation of self
        """
        return {"date":self.date,"store":self.store,"total":self.total}

