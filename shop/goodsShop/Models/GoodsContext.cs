using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using goodsShop.Models;

namespace goodsShop.Models
{
    public class GoodsContext : DbContext
    {
        public GoodsContext(DbContextOptions<GoodsContext> options) : base(options)
        {
        }

        public DbSet<Goods> goods { get; set; }

        public DbSet<Image> Image { get; set; }
    }
}
