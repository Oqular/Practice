using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class User
    {
        public long id { get; set; }
        [Required]
        public string name { get; set; }
        [Required]
        public string surname { get; set; }

        public double rating { get; set; }
        public List<Goods> goods { get; set; }

        [Required]
        public string username { get; set; }
        [Required]
        public string password { get; set; }
        public string role { get; set; }
        public string token { get; set; }
    }
}
