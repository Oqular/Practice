using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace goodsShop.Models
{
    public class Image
    {
        public long id { get; set; }
        [Required]
        public long goodsId { get; set; }
        public byte[] image { get; set; }
    }
}
