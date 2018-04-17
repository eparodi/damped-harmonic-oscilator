function graphics_error

  gp = dlmread('error-gp.csv',';',2,0);
  bm = dlmread('error-bm.csv',';',2,0);
  ve = dlmread('error-ve.csv',';',2,0);

  semilogy(bm(:,1), bm(:,2), 'color', 'r', ';Beeman;');
  hold on;
  semilogy(ve(:,1), ve(:,2), 'color', 'g', ';Verman;');
  hold on;
  semilogy(gp(:,1), gp(:,2), 'color', 'b', ';Gear Prediction;');
  hold on;
  xlabel('Tiempo (s)');
  ylabel('Error');
  h = legend('show');
end
