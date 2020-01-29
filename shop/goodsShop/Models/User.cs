using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class User
    {
        public long id { get; set; }
        public string name { get; set; }
        public string surname { get; set; }
        //public double rating { get; set; }

        public string username { get; set; }
        public string password { get; set; }
        public string role { get; set; }
    }
}
