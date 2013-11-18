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
    
    def __init__(self,_id=100,title="The Title",amount=10.0,filename="some_file.png",
                    category="payment",kind="casual",date="12-12-2012",json=None):
        """
        Can construct self based off of individual parameters or off of a 
        json/dict structure
        """
        if json==None:
            self._id=_id
            self.title=title
            self.amount=amount
            self.filename=filename
            self.category=category
            self.kind=kind
            self.date=date
        else:
            self._id=json["_id"]
            self.title=json["title"]
            self.amount=json["amount"]
            self.filename=json["filename"]
            self.category=json["category"]               
            self.kind=json["kind"]
            self.date=json["date"]
        
    def __eq__(self,other):
       i = self._id == other._id
       t = self.title == other.title
       a = self.amount == other.amount
       f = self.filename == other.filename
       c = self.category == other.category
       k = self.kind == other.kind
       d = self.date == other.date
       return i and t and a and f and c and k and d
    
    def serialize(self):
        """
        returns a dictionary representation of self
        """
        return {"_id":self._id,"title":self.title,"amount":self.amount,
                "filename":self.filename,"category":self.category,
                "kind":self.kind,"date":self.date}

